package com.codershubham.cms.cms.constant;

public class PathConstant {

//    Security Path
    public static final String LOGIN_PATH = "login";
    public static final String LOGOUT_PATH = "logout";
    public static final String FORGOT_PASSWORD_PATH = "/auth/forgot-password";
    public static final String VALIDATE_TOKEN_PATH = "/auth/validate-token";
    public static final String RESET_PASSWORD_PATH = "/auth/reset-password";
//    user path
    public static final String USERS_PATH = "users";
    public static final String ADMIN_PATH = "admin";
    public static final String FACULTY_PATH = "faculty";
    public static final String STUDENTS_PATH = "students";

//  Admin Path
    public static final String DEPARTMENTS_PATH = ADMIN_PATH + "/departments";
    public static final String COURSES_PATH = ADMIN_PATH + "/courses";
    public static final String SUBJECTS_PATH = ADMIN_PATH + "/subjects";
    public static final String SYLLABUS_PATH = ADMIN_PATH + "/syllabus";
    public static final String ROLES_PATH = ADMIN_PATH + "/roles";
    public static final String EXAM_PATH = ADMIN_PATH + "/exam";

//  Faculty Path
    public static final String FACULTY_SYLLABUS_PATH = FACULTY_PATH + "/syllabus";

    public static final String SEMESTER_PATH = "semesters";
    public static final String ASSIGNMENTS_PATH = "assignments";

    public static final String ENROLLMENTS_PATH = "enrollments";
    public static final String SUBJECT_ENROLLMENTS_PATH = "subject-enrollment";
    public static final String FACULTY_SUBJECT_PATH = "faculty-subject";
    public static final String ATTENDANCE_PATH = "attendance";

//    common URL
    public static final String ADD_PATH = "add";
    public static final String DASHBOARD_PATH = "dashboard";

    public static final String UPDATE_PATH = "/update/{id}";
    public static final String DELETE_PATH = "/delete/{id}";
}
