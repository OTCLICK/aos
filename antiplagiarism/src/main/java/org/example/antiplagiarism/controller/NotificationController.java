//package org.example.antiplagiarism.controller;
//
//import org.example.antiplagiarism.dto.NotificationResponse;
//import org.example.antiplagiarism.interfaces.NotificationApi;
//import org.example.antiplagiarism.service.NotificationService;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//public class NotificationController implements NotificationApi {
//
//    private final NotificationService notificationService;
//    private final NotificationModelAssembler notificationModelAssembler;
//
//    public NotificationController(
//            NotificationService notificationService,
//            NotificationModelAssembler notificationModelAssembler
//    ) {
//        this.notificationService = notificationService;
//        this.notificationModelAssembler = notificationModelAssembler;
//    }
//
//    @Override
//    public CollectionModel<EntityModel<NotificationResponse>> getNotificationsForStudent(String studentId) {
//        List<NotificationResponse> notifications = notificationService.getNotificationsForStudent(studentId);
//        return notificationModelAssembler.toCollectionModel(notifications);
//    }
//}
