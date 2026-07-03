package com.sentrypay.backend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sentrypay.backend.domain.user.repository.WalletRepository;
import com.sentrypay.backend.domain.user.entity.WalletEntity;
import org.springframework.web.bind.annotation.PathVariable;
import com.sentrypay.backend.dto.WalletResponse;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class WalletController {


    WalletResponse walletResponse;

    private final WalletRepository walletRepository;

    public WalletController(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }


    @GetMapping("/users/{userId}/wallet")
    public ResponseEntity<WalletResponse> getWalletbyUserId(@PathVariable Long userId){
        
        // return the wallet entity associated with the user id if it exists, otherwise return a 404 not found response
        return walletRepository.findByUserId(userId)
                .map(wallet -> {
                    walletResponse = new WalletResponse(
                        wallet.getWalletId(),
                        wallet.getBalance(),
                        wallet.getCurrency(),
                        wallet.getCreatedAt(),
                        wallet.getUser().getId(),
                        wallet.getUser().getFullname()
                    );
                    return ResponseEntity.ok(walletResponse);
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
