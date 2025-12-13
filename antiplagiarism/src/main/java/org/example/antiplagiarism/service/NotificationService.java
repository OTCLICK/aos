//package org.example.antiplagiarism.service;
//
//import org.example.antiplagiarism.dto.NotificationResponse;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//public class NotificationService {
//
//    public List<NotificationResponse> getNotificationsForStudent(String studentId) {
//        return List.of(
//                new NotificationResponse(
//                        UUID.randomUUID().toString(),
//                        studentId,
//                        "Ваша работа проверена",
//                        "GRADE_POSTED",
//                        LocalDateTime.now().minusHours(1)
//                )
//        );
//    }
//}
