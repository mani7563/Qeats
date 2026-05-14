
package com.crio.qeats.repositoryservices;

import com.crio.qeats.dto.Restaurant;
import com.crio.qeats.exchanges.GetRestaurantsRequest;
import com.crio.qeats.exchanges.GetRestaurantsResponse;
import com.crio.qeats.utils.FixtureHelpers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
@Log4j2
@Service
public class RestaurantRepositoryServiceDummyImpl implements RestaurantRepositoryService {
 

@Autowired
private RestaurantRepositoryService repositoryRepoService;

  // TODO: CRIO_TASK_MODULE_RESTAURANTSAPI - Use this dummy implementation.
  // This function returns a list of restaurants in any lat/long of your choice randomly.
  // It will load some dummy restaurants and change their latitude/longitude near
  // the lat/long you pass. In the next module, once you start using mongodb, you will not use
  // it anymore.
  
  @Override
  public List<Restaurant> findAllRestaurantsCloseBy(
      Double latitude,
      Double longitude,
      LocalTime currentTime,
      Double servingRadiusInKms) {

    List<Restaurant> restaurantList = new ArrayList<>();

    try {
      restaurantList = loadRestaurantsDuringNormalHours();
    } catch (IOException e) {
      log.error("Error loading restaurants", e);
      return restaurantList;
    }

    for (Restaurant restaurant : restaurantList) {
      restaurant.setLatitude(
          latitude + ThreadLocalRandom.current().nextDouble(0.000001, 0.2));

      restaurant.setLongitude(
          longitude + ThreadLocalRandom.current().nextDouble(0.000001, 0.2));
    }

    return restaurantList;
  }
  private List<Restaurant> loadRestaurantsDuringNormalHours() throws IOException {
    String fixture = FixtureHelpers.fixture("fixtures/exchanges/normal_hours_list_of_restaurants.json");

    ObjectMapper objectMapper = new ObjectMapper();

    return objectMapper.readValue(fixture, new TypeReference<List<Restaurant>>() {});
  }
}


