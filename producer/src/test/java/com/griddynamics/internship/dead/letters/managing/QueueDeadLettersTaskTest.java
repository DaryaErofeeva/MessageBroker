package com.griddynamics.internship.dead.letters.managing;

import com.griddynamics.internship.dao.DAOFactory;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class QueueDeadLettersTaskTest {

    @Mock
    private DAOFactory daoFactory;

    @InjectMocks
    private QueueDeadLettersTask queueDeadLettersTask;

}
