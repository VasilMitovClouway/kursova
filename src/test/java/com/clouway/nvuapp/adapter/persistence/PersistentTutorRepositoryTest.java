package com.clouway.nvuapp.adapter.persistence;

import com.google.common.collect.Lists;
import com.clouway.nvuapp.core.Tutor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.clouway.nvuapp.adapter.persistence.ConnectionProvider;
import com.clouway.nvuapp.adapter.persistence.PersistentTutorRepository;
import com.clouway.nvuapp.adapter.persistence.dao.DataStore;

import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Denis Dimitrov <denis.k.dimitrov@gmail.com>.
 */
public class PersistentTutorRepositoryTest {
  private ConnectionProvider connectionProvider = new ConnectionProvider();
  private DataStore dataStore = new DataStore(connectionProvider);
  private PersistentTutorRepository tutors = new PersistentTutorRepository(dataStore);

  @Before
  public void setUp() throws Exception {
    String createTable = "CREATE TABLE TUTORS(\n" +
            "TUTOR_ID VARCHAR (5) UNIQUE PRIMARY KEY NOT NULL,\n" +
            "PASSWORD VARCHAR (10) NOT NULL\n" +
            ")";
    dataStore.update(createTable);

  }

  @After
  public void tearDown() throws Exception {
    String dropTable = "DROP TABLE TUTORS";
    dataStore.update(dropTable);
  }

  @Test
  public void happyPath() throws Exception {
    Tutor tutor = new Tutor("11111", "11111");
    tutors.register(tutor);
    List<Tutor> tutorList = Lists.newArrayList(tutor);
    assertThat(tutorList.get(0), is(tutors.findTutor("11111").get(0)));
  }

  @Test
  public void findWrongTutor() throws Exception {
    Tutor tutor = new Tutor("33333", "33333");
    tutors.register(tutor);
    assertThat((tutors.findTutor("11111")), is(Collections.EMPTY_LIST));
  }

  @Test
  public void noRegisteredTutors() throws Exception {
    assertThat((tutors.findTutor("11111")), is(Collections.EMPTY_LIST));
  }

}
