package ru.nsu.services;

import ru.nsu.model.GDS.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public interface IRaceService {

    /**
     * Получение всех доступных стран для поездок
     *
     * @return - список стран
     */
    List<Country> getCountries();

    /**
     * Получение всех доступных регионов определённой страны.
     *
     * @param countryID - страна, которая нас интересует
     * @return - список регионов
     */
    List<Region> getRegions(@NotNull long countryID);

    /**
     * Получение информации про определённый автовокзал или место посадки/высадки.
     *
     * @param depotId - айди, интересующей нас сущности
     * @return - сущность вокзала
     */
    DepotInfo getDepotInfo(@NotNull long depotId);

    /**
     * Получение списка пунктов отправления из определённого региона.
     *
     * @param regionId - айди региона
     * @return - список пунктов отбытия
     */
    List<Point> getDispatchPoints(@NotNull long regionId);

    /**
     * Получение списка пунктов прибытия из пункта отбытия.
     *
     * @param dispatchPointId - айди спуска отбытия
     * @return - список пунктов прибытия
     */
    List<Point> getArrivalPoints(@NotNull long dispatchPointId);

    /**
     * Получение всех рейсов в определённый день из определённого пункта в другой определённый пункт.
     *
     * @param dispatchPointId - айди пункта отбытия
     * @param arrivalPointId - айди пункта прибытия
     * @param raceDate - интересующая дата
     * @return - список рейсов
     */
    List<Race> getRacesByPointAndDay(@NotNull Long dispatchPointId, @NotNull Long arrivalPointId, @NotNull LocalDate raceDate);

    /**
     * Получение ВСЕЙ информации о рейсе, включая остановки, вокзалы, цены, места и тп.
     *
     * @param raceId - айди интересующего рейса
     * @return - сущность рейса
     */
    RaceFullInfo getRaceFullInfo(@NotNull String raceId);
}
