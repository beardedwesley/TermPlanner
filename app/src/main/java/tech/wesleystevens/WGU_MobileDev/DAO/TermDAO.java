package tech.wesleystevens.WGU_MobileDev.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import tech.wesleystevens.WGU_MobileDev.Entities.Term;

@Dao
public interface TermDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Term term);

    @Delete
    void delete(Term term);

    @Query("SELECT * FROM term_table ORDER BY ID ASC")
    LiveData<List<Term>> getAllTerms();

    @Query("DELETE FROM term_table")
    void deleteAll();

    @Update
    void update(Term term);
}
