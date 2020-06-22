package com.nwdaf.Analytics.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;


@Getter @Setter
@NoArgsConstructor
public class NnwdafEventsSubscription {

    @NotEmpty
    List<@Valid EventSubscription> eventSubscriptions;

    @NotEmpty
    String notificationURI;
}
