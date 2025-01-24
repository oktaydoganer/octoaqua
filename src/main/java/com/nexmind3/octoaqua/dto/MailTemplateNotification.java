package com.nexmind3.octoaqua.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MailTemplateNotification {
    private String email;
    private String templateName;
    private String subject;
    private Map<String, Object> variables;
}
