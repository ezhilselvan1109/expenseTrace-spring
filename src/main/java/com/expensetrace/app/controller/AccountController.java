package com.expensetrace.app.controller;

import com.expensetrace.app.requestDto.AccountRequestDto;
import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.model.Account;
import com.expensetrace.app.response.ApiResponse;
import com.expensetrace.app.service.account.IAccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/accounts")
@Tag(name = "Account", description = "Manage your Account")
public class AccountController {
    private final IAccountService accountService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllAccounts() {
        try {
            List<Account> account = accountService.getAllAccounts();
            return  ResponseEntity.ok(new ApiResponse("Found!", account));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error:", INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addAccount(@RequestBody AccountRequestDto name) {

        try {
            Account theAccount = accountService.addAccount(name);
            return  ResponseEntity.ok(new ApiResponse("Success", theAccount));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/account/{id}/account")
    public ResponseEntity<ApiResponse> getAccountById(@PathVariable Long id){
        try {
            Account theAccount = accountService.getAccountById(id);
            return  ResponseEntity.ok(new ApiResponse("Found", theAccount));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/account/{name}/account")
    public ResponseEntity<ApiResponse> getAccountByName(@PathVariable String name){
        try {
            Account theAccount = accountService.getAccountByName(name);
            return  ResponseEntity.ok(new ApiResponse("Found", theAccount));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @DeleteMapping("/account/{id}/delete")
    public ResponseEntity<ApiResponse> deleteAccount(@PathVariable Long id){
        try {
            accountService.deleteAccountById(id);
            return  ResponseEntity.ok(new ApiResponse("Found", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/account/{id}/update")
    public ResponseEntity<ApiResponse> updateAccount(@PathVariable Long id, @RequestBody AccountRequestDto account) {
        try {
            Account updatedAccount = accountService.updateAccount(account, id);
            return ResponseEntity.ok(new ApiResponse("Update success!", updatedAccount));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
