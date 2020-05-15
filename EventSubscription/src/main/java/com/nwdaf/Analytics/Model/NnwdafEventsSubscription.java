package com.nwdaf.Analytics.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter @Setter
public class NnwdafEventsSubscription {

    List<EventSubscription> eventSubscriptions;
    String notificationURI;
}
