//package org.example.antiplagiarism.storage;
//
//import org.example.antiplagiarism.service.SubmissionService;
//import org.example.antiplagiarism.service.GradingService;
//import jakarta.annotation.PostConstruct;
//import org.springframework.stereotype.Component;
//
//@Component
//public class InitialDataLoader {
//
//    private final SubmissionService submissionService;
//    private final GradingService gradingService;
//
//    public InitialDataLoader(SubmissionService submissionService, GradingService gradingService) {
//        this.submissionService = submissionService;
//        this.gradingService = gradingService;
//    }
//
//    @PostConstruct
//    public void init() {
//        String workId1 = createWork("Курсовая по Java", "student-001", "public class Main { ... }");
//        String workId2 = createWork("Лабораторная по Spring", "student-002", "@RestController ...");
//        String workId3 = createWork("Дипломная работа", "student-001", "Сложный код диплома...");
//
//        gradingService.gradeWork(workId1, new org.example.antiplagiarism.dto.GradeRequest(85, "reviewer-101"));
//        gradingService.gradeWork(workId2, new org.example.antiplagiarism.dto.GradeRequest(92, "reviewer-102"));
//    }
//
//    private String createWork(String title, String studentId, String content) {
//        var request = new org.example.antiplagiarism.dto.WorkSubmissionRequest(title, studentId, content);
//        var response = submissionService.submitWork(request);
//        return response.getWorkId();
//    }
//}