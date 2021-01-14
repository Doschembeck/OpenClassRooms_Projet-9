package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.model.Agent;

import java.util.List;

@Dao
public interface AgentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createAgent(Agent agent);

    @Update
    int updateAgent(Agent agent);

    @Delete
    int deleteAgent(Agent agent);

    @Query("SELECT * FROM Agent WHERE id = :agentId")
    LiveData<Agent> getAgent(long agentId);

    @Query("SELECT * FROM Agent")
    LiveData<List<Agent>> getAllAgent();

}
