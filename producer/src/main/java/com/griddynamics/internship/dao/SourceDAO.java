package com.griddynamics.internship.dao;

import com.griddynamics.internship.models.entities.Consumer;
import com.griddynamics.internship.models.entities.Message;
import com.griddynamics.internship.models.entities.Source;

public interface SourceDAO<E extends Source> extends GenericDAO<E, Integer> {

    int createMessage(E entity, Message message);

    int addConsumer(E entity, Consumer consumer);

    E getEntityByName(String name);

    Message getMessageByIdAndEntityName(String entityName, int messageId);
}
