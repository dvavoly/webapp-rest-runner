package org.example.entity;

import org.example.util.HibernateUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelTest {

    @Test
    void createFile() {
        try (var session = HibernateUtil.openSession()) {
            var tx = session.beginTransaction();

            var file = File.builder()
                    .fileName("testFile3.txt")
                    .fileType("application/json")
                    .fileDownloadUri("http://storage/testFile3.txt")
                    .build();
//
//            var user = User.builder()
//                    .userName("User2")
//                    .build();
//
//
//
            var user = session.find(User.class, 5);
            session.persist(file);
            var event = Event.builder()
                    .fileId(file.getId())
                    .userId(5)
                    .uploadTime(LocalDateTime.now())
                    .build();
            session.persist(event);
            session.persist(user);

            System.out.println(user.getId());
            System.out.println(user.getUserName());
            for (var item : user.getEventEntities()) {
                System.out.println(item);
            }
            tx.commit();
        }
    }
}