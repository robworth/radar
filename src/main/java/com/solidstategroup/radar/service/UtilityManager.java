package com.solidstategroup.radar.service;

import com.solidstategroup.radar.model.Centre;
import com.solidstategroup.radar.model.Consultant;
import com.solidstategroup.radar.model.Country;
import com.solidstategroup.radar.model.DiagnosisCode;
import com.solidstategroup.radar.model.Ethnicity;
import com.solidstategroup.radar.model.Relative;
import com.solidstategroup.radar.model.filter.ConsultantFilter;
import org.jfree.chart.JFreeChart;

import java.util.List;
import java.util.Map;

public interface UtilityManager {

    String getSiteUrl();

    String getPatientViewSiteUrl();

    Centre getCentre(long id);

    List<Centre> getCentres();

    Consultant getConsultant(long id);

    List<Consultant> getConsultants();

    List<Consultant> getConsultants(ConsultantFilter filter);

    List<Consultant> getConsultants(ConsultantFilter filter, int page, int numberPerPage);

    List<Consultant> getConsultantsByCentre(Centre centre);

    void saveConsultant(Consultant consultant) throws Exception;

    void deleteConsultant(Consultant consultant) throws Exception;

    Country getCountry(long id);

    List<Country> getCountries();

    Ethnicity getEthnicityByCode(String ethnicityCode);

    List<Ethnicity> getEthnicities();

    Relative getRelative(long id);

    List<Relative> getRelatives();

    Map<Long, Integer> getPatientCountPerUnitByDiagnosisCode(DiagnosisCode diagnosisCode);

    int getPatientCountByUnit(Centre centre);

    JFreeChart getPatientCountPerUnitChart();
}
