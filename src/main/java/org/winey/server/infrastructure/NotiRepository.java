package org.winey.server.infrastructure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import org.winey.server.domain.feed.Feed;
import org.winey.server.domain.notification.Notification;
import org.winey.server.domain.user.User;

import java.util.List;

public interface NotiRepository extends Repository<Notification,Long>{
    void save(Notification notification);

    List<Notification> findAllByNotiReceiverOrderByCreatedAtDesc(User user);

    List<Notification> findByNotiReceiverAndIsCheckedFalse(User notiReceiver);

    long countByNotiReceiverAndIsCheckedFalse(User notiReceiver);




}