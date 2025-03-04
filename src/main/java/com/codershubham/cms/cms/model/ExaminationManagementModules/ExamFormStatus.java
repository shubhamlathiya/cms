package com.codershubham.cms.cms.model.ExaminationManagementModules;

public enum ExamFormStatus {
    PENDING_APPROVAL, // Student submitted
    FACULTY_APPROVED, FACULTY_REJECTED, // Faculty review
    ACCOUNT_PENDING, FINAL_APPROVED, ACCOUNT_REJECTED // Accounts review
}
