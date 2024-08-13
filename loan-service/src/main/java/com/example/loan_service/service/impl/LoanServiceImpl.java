package com.example.loan_service.service.impl;

import com.example.loan_service.service.LoanService;
import com.example.loan_service.client.DocumentProxy;
import com.example.loan_service.client.EmiProxy;

import com.example.loan_service.entity.Loan;
import com.example.loan_service.exceptions.ClientException;
import com.example.loan_service.exceptions.InvalidDataException;
import com.example.loan_service.model.*;
import com.example.loan_service.repository.LoanRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
@Service
@RequiredArgsConstructor
@Slf4j
public class LoanServiceImpl implements LoanService {
    @NonNull
    private LoanRepository loanRepository;
    @NonNull
    private EmiProxy emiProxyClient;
    @NonNull
    private DocumentProxy documentProxyClient;
    @Value("${installments.value}")
    private int installments;
    public void setInstallments(int installments) {
        this.installments = installments;
    }
    @Override
    public LoanResponseModel applyLoan(LoanRequestModel loanDto,String userID) {
        double assetValue = loanDto.getAssetValue();
        double loanAmount = loanDto.getLoanAmount();
        if ((loanAmount <= 0) || (assetValue<loanAmount)) {
            log.error("Invalid Amount");
            throw new InvalidDataException("Enter valid Amount");
        }
        if(loanDto.getInstallments() > installments){
            log.error("Invalid installment");
            throw new InvalidDataException("Installments should not be greater than 120");
        }
        Loan loan = new Loan().builder().loanNumber(generateLoanNumber()).userId(Integer.valueOf(userID))
                .assetValue(loanDto.getAssetValue()).loanAmount(loanDto.getLoanAmount())
                .installments(loanDto.getInstallments())
                .firstLevelApproval(false).secondLevelApproval(false)
                .loanStatus("pending")
                .build();
        loanRepository.save(loan);
        LoanResponseModel loanResponse = new LoanResponseModel(loan.getLoanNumber(), "http://localhost:9095/document/upload/"+loan.getLoanNumber());
        log.info("Loan applied Successfully");
        return loanResponse;
    }

    private String generateLoanNumber() {
        String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder loanNumber = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int randomIndex = random.nextInt(alphabets.length());
            loanNumber.append(alphabets.charAt(randomIndex));
        }
        int digits = 100000 + random.nextInt(900000);
        loanNumber.append(digits);
        log.info("Generated Loan Number");
        return loanNumber.toString();
    }
    @Override
    public Loan saveLoanDetails(Loan loan) {
        log.info("Loan Details saved in Loan DB");
        return (Loan) loanRepository.save(loan);
    }

    @Override
    public Optional<Loan> getLoanByLoadId(String loanId, String userId) {
        Optional<Loan> loanDetails = Optional.of(loanRepository.findByLoanNumber(loanId)
                .orElseThrow(() -> new InvalidDataException("Invalid Loan Number")));
        if(!Integer.valueOf(userId).equals(loanDetails.get().getUserId())){
            log.error("Invalid Loan Number");
            throw new InvalidDataException("Access Denied");
        }
        return loanDetails;
    }

    @Override
    public Map<Integer, List<UserLoanResponse>> getAllLoans() {
        List<Loan> listOfLoanApplications = loanRepository.findAll();
        Map<Integer, List<UserLoanResponse>> usersLoanDetails = new HashMap<>();
        for (Loan loan : listOfLoanApplications) {
            if (loan.getLoanStatus().equals("pending")) {
                Integer userId = loan.getUserId();
                UserLoanResponse loanResponse = new UserLoanResponse(loan.getLoanNumber(), loan.getLoanStatus(),
                        loan.getLoanAmount());
                if (usersLoanDetails.containsKey(userId)) {
                    usersLoanDetails.get(userId).add(loanResponse);
                } else {
                    List<UserLoanResponse> newUserLoanResponses = new ArrayList<>();
                    newUserLoanResponses.add(loanResponse);
                    usersLoanDetails.put(userId, newUserLoanResponses);
                }
            }
        }
        log.info("Returning list of Loans");
        return usersLoanDetails;
    }

    @Override
    public String updateStatus(LoanStatusModel loanStatusModel,String token) throws ClientException {
        String loanNumber = loanStatusModel.getLoanNumber();
        String loanStatus = loanStatusModel.getLoanStatus();
        Optional<Loan> loanDetails = Optional.of(loanRepository.findByLoanNumber(loanNumber)
                .orElseThrow(() -> new InvalidDataException("Invalid Loan Number")));
        if (!loanDetails.get().getLoanStatus().equals("pending")) {
            log.error("Cannot proceed becoz your status is not in pending");
            throw new InvalidDataException("Cannot proceed further for the Status " + loanDetails.get().getLoanStatus());
        }

        switch (loanStatus) {
            case "approved": {
                if(!isDocumentUploaded(loanNumber,token)) {
                    log.error("Document yet to be uploaded");
                    throw new InvalidDataException("Document yet to be uploaded");
                }
                EmiRequestModel emiCalculateDto = new EmiRequestModel().builder()
                        .loanAmount(loanDetails.get().getLoanAmount()).installments(loanDetails.get().getInstallments())
                        .build();
                loanDetails.get().setPayableAmount(getEmiAmount(emiCalculateDto,token).getTotalPayableAmount());
                loanDetails.get().setEmiAmount(getEmiAmount(emiCalculateDto,token).getEmiAmount());
                loanDetails.get().setInterestRate(getEmiAmount(emiCalculateDto,token).getInterestRate());
                loanDetails.get().setPendingInstallments(loanDetails.get().getInstallments());
                loanDetails.get().setLoanStatus(loanStatus);
                loanDetails.get().setFirstLevelApproval(true);
                loanDetails.get().setStartDate(LocalDate.now());
                loanDetails.get().setEndDate(LocalDate.now().plusMonths(loanDetails.get().getInstallments()));
            }

            break;

            case "required": {
                loanDetails.get().setLoanStatus(loanStatus);
            }
            break;
            case "rejected": {
                loanDetails.get().setReason(loanStatusModel.getReason());
                loanDetails.get().setLoanStatus(loanStatus);

            }
            break;
            default:
                log.error("Enter valid status");
                throw new InvalidDataException("Enter valid status");
        }

        saveLoanDetails(loanDetails.get());
        log.info("Status Updated Successfully");
        return "Status Updated";
    }

    public Boolean getLoanStatus(String loanNumber){
        Optional<Loan> loanDetails = Optional.of(loanRepository.findByLoanNumber(loanNumber)
                .orElseThrow(() -> new InvalidDataException("Invalid Loan Number")));

        String loanStatus = loanDetails.get().getLoanStatus();
        if(loanStatus.equals("pending") || loanStatus.equals("required")){
            log.info("Returning true if status is pending or required");
            return true;
        }
        log.info("Returning false if status is approved");
        return false;
    }
    public EmiResponseModel getEmiAmount(EmiRequestModel emiCalculateDto,String token) throws ClientException {
        ResponseEntity<EmiResponseModel> emiResponse= emiProxyClient.getEMIAmount(token,emiCalculateDto);
        System.out.println(emiResponse.toString());
        if (emiResponse.getBody() != null) {
            log.info("Return the Emi response from emi proxy client");
            return emiResponse.getBody();
        }
        log.error("Failed to fetch EMI response");
        throw new ClientException("Failed to fetch interest rate");
    }

    public boolean isDocumentUploaded(String loanNumber,String token) {
        ResponseEntity<ResponseDto<Boolean>> responseEntity = documentProxyClient.isDocumentUploaded(token, loanNumber);
        if (responseEntity != null && responseEntity.getBody() != null && responseEntity.getBody().getData() != null) {
            log.info("Document Not Uploaded, So it returns true");
            return responseEntity.getBody().getData().booleanValue();
        } else {
            log.info("Document already uploaded");
            return false;
        }
    }
}