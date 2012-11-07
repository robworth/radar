package com.solidstategroup.radar.web.pages.patient;

import com.solidstategroup.radar.model.Centre;
import com.solidstategroup.radar.model.Demographics;
import com.solidstategroup.radar.model.generic.DiseaseGroup;
import com.solidstategroup.radar.model.user.ProfessionalUser;
import com.solidstategroup.radar.model.user.User;
import com.solidstategroup.radar.service.DemographicsManager;
import com.solidstategroup.radar.service.DiagnosisManager;
import com.solidstategroup.radar.web.RadarApplication;
import com.solidstategroup.radar.web.RadarSecuredSession;
import com.solidstategroup.radar.web.dataproviders.DemographicsDataProvider;
import com.solidstategroup.radar.web.pages.BasePage;
import com.solidstategroup.radar.web.pages.patient.alport.AlportPatientPage;
import com.solidstategroup.radar.web.pages.patient.hnf1b.HNF1BPatientPage;
import com.solidstategroup.radar.web.pages.patient.srns.SrnsPatientPage;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.spring.injection.annot.SpringBean;

@AuthorizeInstantiation({User.ROLE_PROFESSIONAL, User.ROLE_SUPER_USER})
public class ExistingPatientsListingPage extends BasePage {

    @SpringBean
    private DemographicsManager demographicsManager;

    @SpringBean
    private DiagnosisManager diagnosisManager;

    public ExistingPatientsListingPage() {

        Centre centre = null;
        AuthenticatedWebSession session = RadarSecuredSession.get();
        if (session.isSignedIn()) {
            if (session.getRoles().hasRole(User.ROLE_SUPER_USER)) {
                // super user can see all patients - centre might be already null but setting to null again for clarity
                centre = null;
            } else if (session.getRoles().hasRole(User.ROLE_PROFESSIONAL)) {
                //get centre from the logged in professional user
                ProfessionalUser professionalUser = (ProfessionalUser) RadarSecuredSession.get().getUser();
                centre = professionalUser.getCentre();
            }
        }

        DemographicsDataProvider demographicsDataProvider = new DemographicsDataProvider(demographicsManager, centre);

        // List existing patients
        add(new DataView<Demographics>("patients", demographicsDataProvider) {
            @Override
            protected void populateItem(Item<Demographics> item) {
                // Populate fields
                Demographics demographics = item.getModelObject();

                // TODO: this is terrible as we need to check disease groups to know where to send it - well done abul
                // TODO: need to implement a patient base page with the constructors needed and then have an enum map
                // TODO: that maps disease ids to the page they need to go to so we dont need all these ifs
                if (demographics.getDiseaseGroup() != null && demographics.getDiseaseGroup().getId().equals(
                        DiseaseGroup.SRNS_DISEASE_GROUP_ID) || demographics.getDiseaseGroup().getId().
                        equals(DiseaseGroup.MPGN_DISEASEGROUP_ID)) {
                    item.add(new BookmarkablePageLink<SrnsPatientPage>("edit", SrnsPatientPage.class,
                            SrnsPatientPage.getParameters(demographics)));
                } else if (demographics.getDiseaseGroup() != null && demographics.getDiseaseGroup().getId().equals(
                        DiseaseGroup.ALPORT_DISEASEGROUP_ID)) {
                    item.add(new BookmarkablePageLink<AlportPatientPage>("edit", AlportPatientPage.class,
                            AlportPatientPage.getPageParameters(demographics)));
                } else if (demographics.getDiseaseGroup() != null && demographics.getDiseaseGroup().getId().equals(
                        DiseaseGroup.HNF1B_DISEASEGROUP_ID)) {
                    item.add(new BookmarkablePageLink<AlportPatientPage>("edit", HNF1BPatientPage.class,
                            HNF1BPatientPage.getPageParameters(demographics)));
                } else {
                    item.add(new BookmarkablePageLink<GenericPatientPage>("edit", GenericPatientPage.class,
                            GenericPatientPage.getPageParameters(demographics)));
                }

                item.add(new Label("surname"), new Label("forename"));
                item.add(DateLabel.forDatePattern("dateOfBirth", RadarApplication.DATE_PATTERN2));
                item.add(new Label("id"));
                item.add(new Label("diagnosis", diagnosisManager.getDiagnosisName(demographics)));

                String patientNumber = "";

                if (demographics.getNhsNumber() != null && demographics.getNhsNumber().length() > 0) {
                    patientNumber = demographics.getNhsNumber();
                } else {
                    patientNumber = demographics.getChiNumber();
                }

                item.add(new Label("nhsNumber", patientNumber));
                item.add(new Label("hospitalNumber"));
                item.add(DateLabel.forDatePattern("dateRegistered", RadarApplication.DATE_PATTERN2));
                item.add(new Label("status.abbreviation"));
                item.add(new Label("renalUnit.name"));
            }
        });
    }
}
