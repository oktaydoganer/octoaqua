package com.nexmind3.octoaqua.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class    UserRegistrationNotification extends MailNotification{
    private Long userId;



}
