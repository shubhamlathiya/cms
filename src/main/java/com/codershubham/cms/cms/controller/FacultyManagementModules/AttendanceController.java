package com.codershubham.cms.cms.controller.FacultyManagementModules;

import com.codershubham.cms.cms.constant.PathConstant;
import com.codershubham.cms.cms.model.FacultyManagementModules.AttendanceModel;
import com.codershubham.cms.cms.model.FacultyManagementModules.FacultyModel;
import com.codershubham.cms.cms.model.FacultyManagementModules.FacultySubjectAssignmentModel;
import com.codershubham.cms.cms.service.FacultyManagementModules.AttendanceService;
import com.codershubham.cms.cms.service.FacultyManagementModules.FacultyService;
import com.codershubham.cms.cms.service.FacultyManagementModules.FacultySubjectAssignmentService;
import com.codershubham.cms.cms.service.StudentManagementModules.StudentService;
import com.codershubham.cms.cms.util.UserRoleUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping(PathConstant.ATTENDANCE_PATH)
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;


    @Autowired
    private StudentService studentService;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private FacultySubjectAssignmentService facultySubjectAssignmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    HttpSession session;

    @Autowired
    private UserRoleUtil userRoleUtil;

    // ✅ Mark Attendance via Form Submission
    @PostMapping("/mark")
    public String markAttendance(@RequestParam Long facultyId, @RequestParam Long subjectId, @RequestParam Long divisionId, @RequestParam int lectureNumber, @RequestParam String selectedTime, @RequestParam Map<String, String> studentAttendances, @RequestParam String attendanceDate) {

//        System.out.println("hy shubham");
//        System.out.println(attendanceDate);
        String startTime = selectedTime.split(" - ")[0].trim();  // Extracts "08:40"
        LocalTime lectureTime = LocalTime.parse(startTime); // Parse only start time

        LocalDate attendanceDate1 = LocalDate.parse(attendanceDate, DateTimeFormatter.ISO_DATE);


//        Iterate over submitted student attendance data
        for (Map.Entry<String, String> entry : studentAttendances.entrySet()) {
            if (entry.getKey().contains(".studentId")) {
                Long studentId = Long.parseLong(entry.getValue());

                // Determine if student was marked present
                String presentKey = entry.getKey().replace(".studentId", ".present");
                boolean isPresent = studentAttendances.containsKey(presentKey);

                // Call service method to mark attendance
                attendanceService.markAttendance(studentId, subjectId, facultyId, divisionId, lectureNumber, lectureTime, isPresent ? "Present" : "Absent", attendanceDate1);
            }
        }

        return "redirect:/faculty/subjects/" + facultyId;
    }


    @GetMapping("/take/{assignmentId}")
    public String getAttendanceData(@PathVariable("assignmentId") Long assignmentId, Model model) {
        try {
            // Fetch faculty assignment details
            FacultySubjectAssignmentModel assignment = facultySubjectAssignmentService.getAssignmentById(assignmentId);
            if (assignment == null) {
                model.addAttribute("error", "Invalid Assignment ID!");
                return "FacultyManagement/faculty/take-attendance";
            }

            // Get students by division (minimal details)
            List<Map<String, Object>> students = studentService.getStudentsByDivision(assignment.getDivision().getId()).stream().map(student -> {
                Map<String, Object> studentMap = new HashMap<>(); // Create a new HashMap
                studentMap.put("id", student.getId());
                studentMap.put("name", (student.getFirstName() != null ? student.getFirstName() : "") + " " + (student.getLastName() != null ? student.getLastName() : ""));
                return studentMap; // Return the map
            }).collect(Collectors.toList());

            // Calculate total lectures based on subject credits
            int totalLectures = assignment.getSubject().getCredits() * 15;
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            // Fetch attendance records safely
            Map<Integer, Object> attendanceMap = new HashMap<>();
            for (int i = 1; i <= totalLectures; i++) {
                boolean isRecorded = attendanceService.isAttendanceTaken(assignmentId, i);
                if (isRecorded) {
                    List<AttendanceModel> attendanceList = attendanceService.getAttendanceDetails(assignmentId, i);

                    if (!attendanceList.isEmpty()) {
                        String attendanceDate = attendanceList.get(0).getAttendanceDate() != null ? attendanceList.get(0).getAttendanceDate().format(dateFormatter) : "N/A";
                        String attendanceTime = attendanceList.get(0).getAttendanceTime() != null ? attendanceList.get(0).getAttendanceTime().format(timeFormatter) : "N/A";

                        List<Map<String, Object>> attendanceRecords = attendanceList.stream().map(attendance -> {
                            Map<String, Object> recordMap = new HashMap<>();
                            recordMap.put("studentId", attendance.getStudent() != null ? attendance.getStudent().getId() : null);
                            recordMap.put("status", attendance.getStatus() != null ? attendance.getStatus() : false);
                            return recordMap;
                        }).collect(Collectors.toList());

                        // Serialize attendanceRecords to JSON
                        String jsonRecords;
                        try {
                            jsonRecords = objectMapper.writeValueAsString(attendanceRecords);
                        } catch (JsonProcessingException e) {
                            jsonRecords = "[]"; // Handle error
                        }

                        attendanceMap.put(i, Map.of("date", attendanceDate, "time", attendanceTime, "records", jsonRecords // Store the JSON string
                        ));
                    }
                } else {
                    attendanceMap.put(i, false);
                }
            }

            // Add data to the model for the Thymeleaf template
            model.addAttribute("assignment", Map.of("id", assignment.getId(), "faculty", Map.of("id", assignment.getFaculty().getFacultyId(), "name", assignment.getFaculty().getFirstName()), "subject", Map.of("id", assignment.getSubject().getSubjectid(), "name", assignment.getSubject().getSubjectName(), "credits", assignment.getSubject().getCredits()), "division", Map.of("id", assignment.getDivision().getId(), "name", assignment.getDivision().getName()), "semester", Map.of("id", assignment.getSemester().getId(), "name", assignment.getSemester().getName())));

            model.addAttribute("students", students);
            model.addAttribute("totalLectures", totalLectures);
            model.addAttribute("attendanceMap", attendanceMap);

            String userRole = userRoleUtil.getUserRole(session);
            model.addAttribute("userRole", userRole);

            Long userId = (Long) session.getAttribute("userId");
            FacultyModel faculty = facultyService.getFacultyByUserId(userId);
            model.addAttribute("faculty", faculty);
            return "FacultyManagement/faculty/take-attendance";  // Return Thymeleaf template
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while fetching attendance data.");
            model.addAttribute("details", e.getMessage());
            return "FacultyManagement/faculty/take-attendance";
        }
    }
}
