package org.wella.dao;

import org.wella.entity.VehicleGrabInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/12.
 */
public interface VehicleGrabInfoDao {

    int createVehicleGrabInfo(VehicleGrabInfo vehicleGrabInfo);

    List<Map<String,Object>> listVehicleGrabInfoByConditions(Map query);

    Integer deleteDriver(Long id);


    List<Map<String,Object>> selectDriver(Long logisticsId);


    Integer insertVehicleGrabInfo(List list);

}
