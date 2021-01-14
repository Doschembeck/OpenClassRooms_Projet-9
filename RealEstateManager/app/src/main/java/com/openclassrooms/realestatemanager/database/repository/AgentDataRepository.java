package com.openclassrooms.realestatemanager.database.repository;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.openclassrooms.realestatemanager.database.dao.AgentDao;
import com.openclassrooms.realestatemanager.model.Agent;

import java.util.List;

public class AgentDataRepository {

    private final AgentDao agentDao;

    public AgentDataRepository(AgentDao agentDao){
        this.agentDao = agentDao;
    }

    // GET ALL AGENT
    public LiveData<List<Agent>> getAllAgent(){
        return this.agentDao.getAllAgent();
    }

    // GET AGENT
    public LiveData<Agent> getAgent(long agentId){
        return this.agentDao.getAgent(agentId);
    }

    // CREATE
    public long createAgent(Agent agent){
        return this.agentDao.createAgent(agent);
    }

    // UPDATE
    public long updateAgent(Agent agent){
        return this.agentDao.updateAgent(agent);
    }

    // DELETE
    public long deleteAgent(Agent agent){
        return this.agentDao.deleteAgent(agent);
    }

}
