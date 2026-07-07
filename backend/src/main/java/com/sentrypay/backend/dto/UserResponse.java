package com.sentrypay.backend.dto;

import com.sentrypay.backend.dto.UserResponse.UserDetails;
import java.util.List;



public record UserResponse(

        List<UserDetails> userDetails
) {


    public record UserDetails(
        Long userId,
        String userName,
        String userEmail,
        String userFullName,
        String userPhoneNumber
    ) {}

}
