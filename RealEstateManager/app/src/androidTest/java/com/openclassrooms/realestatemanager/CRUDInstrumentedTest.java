package com.openclassrooms.realestatemanager;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.model.Address;
import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.model.NearbyPOI;
import com.openclassrooms.realestatemanager.model.Parameter;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.model.Property;
import com.openclassrooms.realestatemanager.model.PropertyNearbyPoiJoin;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CRUDInstrumentedTest {

    // FOR DATA
    private RealEstateManagerDatabase database;

    // DATA SET FOR TEST
    private static final long AGENT_ID = 999989799;
    private static final Agent AGENT_DEMO = new Agent(AGENT_ID, "Thomas", "Deschombeck", "url//picture");
    private static final long PROPERTY_ID = 999979799;
    private static final Property PROPERTY_DEMO = new Property(PROPERTY_ID, 1, 100000.0, 1000.0, 100, 7, 2, "description", 1f, "Lyon", "url//mainPicture", 2, AGENT_ID, new Date(), new Date(), new Date());
    private static final long PROPERTY_ID2 = 999979798;
    private static final Property PROPERTY_DEMO2 = new Property(PROPERTY_ID2, 1, 200000.0, 1000.0, 100, 7, 2, "description", 1f, "Lyon", "url//mainPicture", 2, AGENT_ID, new Date(), new Date(), new Date());
    private static final long ADDRESS_ID = 999969799;
    private static final Address ADDRESS_DEMO = new Address(ADDRESS_ID, PROPERTY_ID, "2", "Rue de la Rouette", "Massieux", "01600", "France", 10.0, 10.0);
    private static final long PHOTO_ID = 999959799;
    private static final Photo PHOTO_DEMO = new Photo(PHOTO_ID,PROPERTY_ID, "url//picture", "Description photo");
    private static final long POI_ID = 999949799;
    private static final NearbyPOI POI_DEMO = new NearbyPOI(POI_ID, "Ecole");
    private static final PropertyNearbyPoiJoin POIJOIN_DEMO = new PropertyNearbyPoiJoin(PROPERTY_ID, POI_ID);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                RealEstateManagerDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void filterTest() throws InterruptedException {
        insertAndGetAgent();

        this.database.propertyDao().createProperty(PROPERTY_DEMO);
        this.database.propertyDao().createProperty(PROPERTY_DEMO2);

        Parameter parameter = new Parameter();
        parameter.setPriceMax(120000);

        List<Property> propertiesBefore = this.database.propertyDao().getAllProperty();
        List<Property> propertiesAfter = LiveDataTestUtil.getValue(this.database.propertyDao().searchProperties(parameter.getParamsFormatted()));

        assertEquals(2, propertiesBefore.size());
        assertEquals(1, propertiesAfter.size());
    }

    // Create
    @Test
    public void insertAndGetAgent() throws InterruptedException {
        // BEFORE : Adding a new Property
        this.database.AgentDao().createAgent(AGENT_DEMO);

        // TEST
        Agent agent = LiveDataTestUtil.getValue(this.database.AgentDao().getAgent(AGENT_ID));
        assertTrue(agent.getFirstname().equals(AGENT_DEMO.getFirstname()) && agent.getLastname().equals(AGENT_DEMO.getLastname()) && agent.getId() == AGENT_ID);
    }

    @Test
    public void insertAndGetProperty() throws InterruptedException {
        // BEFORE : Adding a new Property
        insertAndGetAgent();
        this.database.propertyDao().createProperty(PROPERTY_DEMO);

        // TEST
        Property user = LiveDataTestUtil.getValue(this.database.propertyDao().getProperty(PROPERTY_ID));
        assertTrue(user.getCity().equals(PROPERTY_DEMO.getCity()) && user.getPrice().equals(PROPERTY_DEMO.getPrice()) && user.getId() == PROPERTY_ID);
    }

    @Test
    public void insertAndGetAddress() throws InterruptedException {
        // BEFORE : Adding a new address
        insertAndGetProperty();
        this.database.addressDao().createAddress(ADDRESS_DEMO);

        // TEST
        Address address = LiveDataTestUtil.getValue(this.database.addressDao().getAddress(ADDRESS_ID));
        assertTrue(address.getCompleteAddress().equals(ADDRESS_DEMO.getCompleteAddress()) && address.getId() == ADDRESS_ID);
    }

    @Test
    public void insertAndGetPhoto() throws InterruptedException {
        // BEFORE : Adding a new Photo
        insertAndGetProperty();
        this.database.PhotoDao().createPhoto(PHOTO_DEMO);

        // TEST
        Photo photo = LiveDataTestUtil.getValue(this.database.PhotoDao().getPhoto(PHOTO_ID));
        assertTrue(photo.getUrlPicture().equals(PHOTO_DEMO.getUrlPicture()) && photo.getId() == PHOTO_ID);
    }

    @Test
    public void insertAndGetNearbyPoi() throws InterruptedException {
        // BEFORE : Adding a new NearbyPOI
        this.database.NearbyPoiDao().createNearbyPOI(POI_DEMO);

        // TEST
        NearbyPOI nearbyPOI = LiveDataTestUtil.getValue(this.database.NearbyPoiDao().getNearbyPOI(POI_ID));
        assertTrue(nearbyPOI.getName().equals(POI_DEMO.getName()) && nearbyPOI.getId() == POI_ID);
    }

    @Test
    public void insertAndGetNearbyPoiJoin() throws InterruptedException {
        // BEFORE : Adding a new NearbyPOI
        insertAndGetProperty();
        insertAndGetNearbyPoi();
        this.database.PropertyNearbyPoiJoinDao().createPropertyNearbyPoiJoin(POIJOIN_DEMO);

        // TEST
        NearbyPOI nearbyPOI = LiveDataTestUtil.getValue(this.database.PropertyNearbyPoiJoinDao().getNearbyPoiForProperty(PROPERTY_ID)).get(0);
        assertTrue(nearbyPOI.getName().equals(POI_DEMO.getName()) && nearbyPOI.getId() == POI_ID);

        Property property = LiveDataTestUtil.getValue(this.database.PropertyNearbyPoiJoinDao().getPropertyForNearbyPoi(POI_ID)).get(0);
        assertTrue(property.getCity().equals(PROPERTY_DEMO.getCity()) && property.getPrice().equals(PROPERTY_DEMO.getPrice()) && property.getId() == PROPERTY_ID);

    }

    // Update
    @Test
    public void insertAndUpdateProperty() throws InterruptedException {
        // BEFORE : Adding demo Property

        insertAndGetProperty();
        // Update the property
        Property propertyUpdated = PROPERTY_DEMO;
        propertyUpdated.setCity("cityUpdated");
        this.database.propertyDao().updateProperty(propertyUpdated);

        // Get propertyUpdated in BDD
        Property propertyGet = LiveDataTestUtil.getValue(this.database.propertyDao().getProperty(PROPERTY_ID));

        // TEST
        assertTrue(propertyGet.getCity().equals("cityUpdated") && propertyGet.getId() == PROPERTY_ID);
    }
    @Test
    public void insertAndUpdateAddress() throws InterruptedException {
        // BEFORE : Adding demo Property
        insertAndGetAddress();
        // Update the property
        Address addressUpdated = ADDRESS_DEMO;
        addressUpdated.setCity("cityUpdated");
        this.database.addressDao().updateAddress(addressUpdated);

        // Get propertyUpdated in BDD
        Address addressGet = LiveDataTestUtil.getValue(this.database.addressDao().getAddress(ADDRESS_ID));

        // TEST
        assertTrue(addressGet.getCity().equals("cityUpdated") && addressGet.getId() == ADDRESS_ID);
    }

    // Delete
    @Test
    public void insertAndDeleteProperty() throws InterruptedException {
        // BEFORE : Adding demo Property
        insertAndGetProperty();
        List<Property> propertiesBeforeDeleting = this.database.propertyDao().getAllProperty();

        Property propertyAdded = LiveDataTestUtil.getValue(this.database.propertyDao().getProperty(PROPERTY_ID));
        this.database.propertyDao().deleteProperty(propertyAdded.getId());

        //TEST
        List<Property> propertiesAfterDeleting = this.database.propertyDao().getAllProperty();
        assertTrue(propertiesAfterDeleting.isEmpty());
    }

}
