package ru.nsu.services.interfaces;

import org.springframework.cache.annotation.Cacheable;
import ru.nsu.model.GDS.*;
import ru.nsu.model.constants.DocumentTypes;

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
    @Cacheable(value = "allRegionsInCountry")
    List<Region> getRegions(@NotNull long countryID);

    /**
     * Получение информации про определённый автовокзал или место посадки/высадки.
     *
     * @param depotId - айди, интересующей нас сущности
     * @return - сущность вокзала
     */
    @Cacheable(value = "depotInfo", key = "#depotId")
    DepotInfo getDepotInfo(@NotNull long depotId);

    /**
     * Получение всех пунктов прибытия для РФ.
     *
     * @return - список пунктов отбытия
     */
    @Cacheable(value = "allDispatchPoint")
    List<Point> getAllDispatchPoints();


    /**
     * Получение списка пунктов отправления из определённого региона.
     *
     * @param regionId - айди региона
     * @return - список пунктов отбытия
     */
    @Cacheable(value = "allDispatchPoint", key = "#regionId")
    List<Point> getDispatchPointsForRegion(@NotNull long regionId);

    /**
     * Получение списка пунктов прибытия из пункта отбытия.
     *
     * @param dispatchPointId - айди спуска отбытия
     * @return - список пунктов прибытия
     */
    @Cacheable(value = "allArrivalPoint", key = "#dispatchPointId")
    List<Point> getArrivalPoints(@NotNull long dispatchPointId);

    /**
     * Получение всех рейсов в определённый день из определённого пункта в другой определённый пункт.
     *
     * @param dispatchPointId - айди пункта отбытия
     * @param arrivalPointId  - айди пункта прибытия
     * @param raceDate        - интересующая пользователя дата
     * @return - список рейсов
     */
    @Cacheable(value = "allRacesInADayFromPointToPoint", key = "{#dispatchPointId, #arrivalPointId, #raceDate}")
    List<Race> getRacesByPointAndDay(@NotNull Long dispatchPointId, @NotNull Long arrivalPointId, @NotNull LocalDate raceDate);

    /**
     * Получение ВСЕЙ информации о рейсе, включая остановки, вокзалы, цены, места и тп.
     *
     * @param raceId - айди интересующего рейса
     * @return - сущность рейса
     */
    @Cacheable(value = "raceInfo", key = "#raceId")
    RaceFullInfo getRaceFullInfo(@NotNull String raceId);

    /**
     * Получение список остановки определённого рейса по айди этого рейса.
     *
     * @param raceId - айди рейса
     * @return - список остановок рейса.
     */
    List<Stop> getRaceStops(@NotNull String raceId);

    /**
     * Получение информации о занятых/свободных местах на рейс.
     *
     * @param raceId - айди рейса
     * @return - список информации о местах
     */
    List<Seat> getRaceSeats(@NotNull String raceId);

    /**
     * Получение информации о типах билетов, доступных на определённый рейс.
     *
     * @param raceId - айди рейса
     * @return - список из с информации о каждом типе билета с ценой.
     */
    List<TicketType> getTicketTypes(@NotNull String raceId);

    /**
     * Получение информации о перевозчике, предоставляющим рейс.
     *
     * @param raceId - айди рейса
     * @return - информация о перевозчике, включая ИНН, ФИО руководителя, адрес и сайт организация и тп.
     */
    Carrier getRaceCarrier(@NotNull String raceId);

    /**
     * Получение информации и типах документов, доступных на рейс.
     *
     * @param raceId - айди рейса
     * @return - список документов, доступных документов с информации о их преимуществах, если они имеются.
     */
    List<DocumentTypes> getRaceAvailableDocuments(@NotNull String raceId);
}
