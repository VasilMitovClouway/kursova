package com.clouway.nvuapp.adapter.persistence;

import com.clouway.nvuapp.core.TutorRepository;
import com.clouway.nvuapp.core.Tutor;
import com.clouway.nvuapp.adapter.persistence.dao.DataStore;

import java.util.List;

/**
 * @author Denis Dimitrov <denis.k.dimitrov@gmail.com>.
 */
public class PersistentTutorRepository implements TutorRepository {
  private DataStore dataStore;

  public PersistentTutorRepository(DataStore dataStore) {
    this.dataStore = dataStore;
  }

  @Override
  public void register(Tutor tutor) {
    String query = "INSERT INTO TUTORS VALUES (?,?)";
    dataStore.update(query, tutor.tutorId, tutor.password);
  }

  public List<Tutor> findTutor(String id) {
    String fetchId = "SELECT * FROM TUTORS WHERE TUTOR_ID='" + id + "'";
    return dataStore.fetchRows(fetchId, resultSet -> (new Tutor(resultSet.getString(1), resultSet.getString(2))));
  }

  @Override
  public List<Tutor> allTutors() {
    String query="SELECT * FROM TUTORS";
    return dataStore.fetchRows(query, resultSet -> (new Tutor(resultSet.getString(1), resultSet.getString(2))));
  }
}
