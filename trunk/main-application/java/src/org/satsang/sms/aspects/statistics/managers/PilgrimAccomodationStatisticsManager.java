/**
 * 
 */
package org.satsang.sms.aspects.statistics.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.satsang.sms.aspects.statistics.ColonyStats;
import org.satsang.sms.aspects.statistics.QuarterStats;
import org.satsang.sms.aspects.statistics.SadanStats;
import org.satsang.sms.aspects.statistics.dao.IPilgrimStatisticsDao;
import org.satsang.sms.aspects.statistics.interfaces.IAccomodationStatistics;
import org.satsang.sms.aspects.statistics.interfaces.IPilgrimStatistics;
import org.satsang.sms.core.activities.tvc.TvcMember;

/**
 * @author Kanika
 * 
 */
public class PilgrimAccomodationStatisticsManager implements
		IPilgrimStatistics, IAccomodationStatistics {

	private IPilgrimStatisticsDao daoHandler;
	private HashMap<String, HashMap<String, ?>> headcountMap;
	private HashMap<String, HashMap<String, ?>> vacancyMap;
	private boolean loadFlag;
	private static Logger log = Logger.getLogger(PilgrimAccomodationStatisticsManager.class);
	
	/**
	 * @param loadFlag
	 *            the loadFlag to set
	 */
	@Override
	public void setLoadFlag(boolean loadFlag) {
		this.loadFlag = loadFlag;
	}

	/**
	 * @param daoHandler
	 *            the daoHandler to set
	 */
	@Override
	public void setDaoHandler(IPilgrimStatisticsDao daoHandler) {
		this.daoHandler = daoHandler;
	}

	public void init() {
		log.info("Initializing PilgrimAccomodationStatisticsManager.");
		if (loadFlag) {
			log.info("Loading the capacities of private quarters, sabha quarters and dormitories.");
			headcountMap = new HashMap<String, HashMap<String, ?>>();
			vacancyMap = new HashMap<String, HashMap<String, ?>>();
			populateMapsWithAccomodationTypes();
			populateMapsWithSQStats();
			populateMapsWithPQandODStats();
			populateMapsWithSadanStats();
			log.info("Loaded Dayalbagh quarters and sadans statistics.");
		}
	}
	
	@SuppressWarnings("unchecked")
	public Object getSadanStatistic(ProceedingJoinPoint call) {
		log.info("Getting the occupancy in the dormitories.");
		if (!loadFlag) {
			return null;
		}
		return (HashMap<String, SadanStats>) headcountMap.get(DORMITORIES);
	}

	@SuppressWarnings("unchecked")
	public Object getSabhaQuarterVacancies(ProceedingJoinPoint call, String colonyName, int noOfPeople) {
		log.info("Getting the vacancies in the sabha quarters.");
		
		if (!loadFlag) {
			return null;
		}
		HashMap<Integer, List<String>> vacanctHousesMap = new HashMap<Integer, List<String>>();
		HashMap<Integer, List<String>> vacancyList = (HashMap<Integer, List<String>>) vacancyMap.get(SABHA_QUARTERS).get(colonyName);

		// get max vacancy in the colony
		int maxVacancy = 0;
		Iterator<Integer> vacancyListIter = vacancyList.keySet().iterator();
		while (vacancyListIter.hasNext()) {
			int vacancy = vacancyListIter.next();
			if (vacancy > maxVacancy) {
				maxVacancy = vacancy;
			}
		}

		for (int index = noOfPeople; index <= maxVacancy; index++) {
			List<String> houseList = vacancyList.get(index);
			if (houseList != null && !houseList.isEmpty()) {
				vacanctHousesMap.put(index, houseList);
			}
		}
		return vacanctHousesMap;
	}

	@SuppressWarnings("unchecked")
	public void update(TvcMember tvc, String bhandaraId) {
		
		if (!loadFlag || tvc.getTvcId() == null) {
			return;
		}
		String stayCategory = tvc.getStayCategory();
		int noOfChildren = tvc.getNoOfChildren();
		String gender = tvc.getGender();
		String localColony = tvc.getLocalColony();
		String localSadan = tvc.getLocalSadan();
		String localAddr = tvc.getLocalAddress();

		int totalNoOfPeople = 1 + noOfChildren;

		if (stayCategory.equals(PRIVATE_QUARTERS)) {
			log.info("Updating the vacancies in private quarters.");
			HashMap<String, ColonyStats> PQMap = (HashMap<String, ColonyStats>) headcountMap.get(PRIVATE_QUARTERS);
			ColonyStats colonyStat = PQMap.get(localColony);
			if (colonyStat == null) {
				colonyStat = new ColonyStats();
			}
			colonyStat.setChildCount(colonyStat.getChildCount() + noOfChildren);
			if (gender.equals("Male")) {
				colonyStat.setMaleCount(colonyStat.getMaleCount() + 1);
			} else if (gender.equals("Female")) {
				colonyStat.setFemaleCount(colonyStat.getFemaleCount() + 1);
			}
			colonyStat.setTotalOccupancy(colonyStat.getTotalOccupancy()	+ totalNoOfPeople);
			
		} else if (stayCategory.equals(OUTSIDE_DAYALBAGH)) {
			log.info("Updating the occupancy outside dayalbagh.");
			HashMap<String, ColonyStats> ODMap = (HashMap<String, ColonyStats>) headcountMap.get(OUTSIDE_DAYALBAGH);
			ColonyStats colonyStat = ODMap.get(localColony);
			if (colonyStat == null) {
				colonyStat = new ColonyStats();
			}
			colonyStat.setChildCount(colonyStat.getChildCount() + noOfChildren);
			if (gender.equals("Male")) {
				colonyStat.setMaleCount(colonyStat.getMaleCount() + 1);
			} else if (gender.equals("Female")) {
				colonyStat.setFemaleCount(colonyStat.getFemaleCount() + 1);
			}
			colonyStat.setTotalOccupancy(colonyStat.getTotalOccupancy()	+ totalNoOfPeople);
			
		} else if (stayCategory.equals(DORMITORIES)) {
			log.info("Updating the head count in the sadans.");
			HashMap<String, SadanStats> sadanMap = (HashMap<String, SadanStats>) headcountMap.get(DORMITORIES);
			SadanStats sadanStat = sadanMap.get(localSadan);
			
			if(sadanStat != null){
				sadanStat.setChildCount(sadanStat.getChildCount() + noOfChildren);	
				if (gender.equals("Male")) {
					sadanStat.setMaleCount(sadanStat.getMaleCount() + 1);
				} else if (gender.equals("Female")) {
					sadanStat.setFemaleCount(sadanStat.getFemaleCount() + 1);
				}	
				sadanStat.setOccupancy(sadanStat.getOccupancy() + totalNoOfPeople);
				sadanStat.setVacancy(sadanStat.getVacancy() - totalNoOfPeople);
			}
		} else if (stayCategory.equals(SABHA_QUARTERS)) {
			log.info("Updating the capacity in sabha quarters..");
			HashMap<String, HashMap<Integer, List<String>>> SQVacancyMap = (HashMap<String, HashMap<Integer, List<String>>>) vacancyMap
					.get(SABHA_QUARTERS);
			HashMap<String, List<Object>> SQMap = (HashMap<String, List<Object>>) headcountMap.get(SABHA_QUARTERS);

			List<Object> colonyInfo = SQMap.get(localColony);
			if(colonyInfo != null){
				HashMap<String, QuarterStats> quarterMap = (HashMap<String, QuarterStats>) colonyInfo.get(0);
				QuarterStats quarterStats = quarterMap.get(localAddr);
				if(quarterStats != null){
					int vacancy = quarterStats.getQuarterVacancy();
					quarterStats.setQuarterOccupancy(quarterStats.getQuarterOccupancy() + totalNoOfPeople);
					quarterStats.setQuarterVacancy(vacancy - totalNoOfPeople);
		
					HashMap<Integer, List<String>> vacancyList = SQVacancyMap.get(localColony);
					vacancyList.get(vacancy).remove(localAddr);
					if(vacancyList.get(vacancy - totalNoOfPeople) == null){
						vacancyList.put(vacancy - totalNoOfPeople, new ArrayList<String>());
					}
					vacancyList.get(vacancy - totalNoOfPeople).add(localAddr);
				}
			}
		}
	}

	public void editAccomodation(TvcMember oldTvc, TvcMember newTvc){
		
		if(oldTvc.getStayCategory().equals(newTvc.getStayCategory()) 
				&& (!hasColonyChanged(oldTvc.getLocalColony(), newTvc.getLocalColony()) 
						|| !hasSadanChanged(oldTvc.getLocalSadan(), newTvc.getLocalSadan()))
				&& (oldTvc.getLocalAddress().equals(newTvc.getLocalAddress()))){
			return;
		}
		
		if(isMonitoredStayCategory(oldTvc.getStayCategory()) || isMonitoredStayCategory(newTvc.getStayCategory())){
			
		}
	}
	
	private boolean hasColonyChanged(String oldColony, String newColony){
		boolean hasChanged = false;
		if((oldColony == null) ^ (newColony == null)){
			hasChanged = true;
		}
		if(oldColony != null && !oldColony.equals(newColony)){
			hasChanged = true;
		}
		return hasChanged;
	}
	
	private boolean hasSadanChanged(String oldSadan, String newSadan){
		boolean hasChanged = false;
		if((oldSadan == null) ^ (newSadan == null)){
			hasChanged = true;
		}
		if(oldSadan != null && !oldSadan.equals(newSadan)){
			hasChanged = true;
		}
		return hasChanged;
	}
	
	private boolean isMonitoredStayCategory(String stayCategory){
		return DORMITORIES.equals(stayCategory) || SABHA_QUARTERS.equals(stayCategory);
	}
	
	private void populateMapsWithAccomodationTypes() {
		HashMap<String, ColonyStats> PQMap = new HashMap<String, ColonyStats>();
		PQMap.put(KARYAVIR_NAGAR, null);
		PQMap.put(SARAN_ASHRAM_NAGAR, null);
		PQMap.put(PREM_NAGAR, null);
		PQMap.put(VIDYUT_NAGAR, null);
		PQMap.put(SWET_NAGAR, null);
		PQMap.put(SOAMI_NAGAR, null);
		headcountMap.put(PRIVATE_QUARTERS, PQMap);

		HashMap<String, List<Object>> SQMap = new HashMap<String, List<Object>>();
		SQMap.put(KARYAVIR_NAGAR, new ArrayList<Object>());
		SQMap.put(SARAN_ASHRAM_NAGAR, new ArrayList<Object>());
		SQMap.put(PREM_NAGAR, new ArrayList<Object>());
		SQMap.put(VIDYUT_NAGAR, new ArrayList<Object>());
		SQMap.put(SWET_NAGAR, new ArrayList<Object>());
		SQMap.put(SOAMI_NAGAR, new ArrayList<Object>());
		headcountMap.put(SABHA_QUARTERS, SQMap);

		HashMap<String, SadanStats> sadanMap = new HashMap<String, SadanStats>();
		sadanMap.put(DORMITORY, null);
		sadanMap.put(YOUTH_HOSTEL, null);
		sadanMap.put(YATRI_SADAN, null);
		sadanMap.put(NDB_SCHOOL, null);
		sadanMap.put(ODB_SCHOOL, null);
		sadanMap.put(PILGRIM_SHED, null);
		sadanMap.put(OLD_EXHIBITION_COMPLEX, null);
		// FOR BASANT ONLY
		sadanMap.put(PF_CAMP, null);
		sadanMap.put(VN_CAMP, null);
		sadanMap.put(DAIRY_CAMP, null);
		sadanMap.put(UPRSA, null);
		sadanMap.put(ROOMS, null);
		//
		headcountMap.put(DORMITORIES, sadanMap);

		HashMap<String, ColonyStats> ODMap = new HashMap<String, ColonyStats>();
		ODMap.put(ADAN_BAGH, null);
		ODMap.put(RADHA_NAGAR, null);
		ODMap.put(OTHER, null);
		headcountMap.put(OUTSIDE_DAYALBAGH, ODMap);

		HashMap<String, HashMap<Integer, List<String>>> SQVacancyMap = new HashMap<String, HashMap<Integer, List<String>>>();
		SQVacancyMap.put(KARYAVIR_NAGAR, new HashMap<Integer, List<String>>());
		SQVacancyMap.put(SARAN_ASHRAM_NAGAR,
				new HashMap<Integer, List<String>>());
		SQVacancyMap.put(PREM_NAGAR, new HashMap<Integer, List<String>>());
		SQVacancyMap.put(VIDYUT_NAGAR, new HashMap<Integer, List<String>>());
		SQVacancyMap.put(SWET_NAGAR, new HashMap<Integer, List<String>>());
		SQVacancyMap.put(SOAMI_NAGAR, new HashMap<Integer, List<String>>());
		vacancyMap.put(SABHA_QUARTERS, SQVacancyMap);
	}

	@SuppressWarnings("unchecked")
	private void populateMapsWithSQStats() {

		List<QuarterStats> quarterStats = daoHandler.getSQAccomodationStats();

		HashMap<String, HashMap<Integer, List<String>>> SQVacancyMap = (HashMap<String, HashMap<Integer, List<String>>>) vacancyMap
				.get(SABHA_QUARTERS);
		HashMap<String, List<Object>> SQMap = (HashMap<String, List<Object>>) headcountMap
				.get(SABHA_QUARTERS);

		String colonyName = null;
		HashMap<Integer, List<String>> vacancyList = null;
		List<Object> colonyList = null;
		HashMap<String, QuarterStats> quarterMap = null;
		for (QuarterStats quarter : quarterStats) {
			if (quarter.getQuarterOccupancy() == null) {
				quarter.setQuarterOccupancy(0);
				quarter.setQuarterVacancy(quarter.getQuarterCapacity());
			}
			if (!quarter.getColonyName().equals(colonyName)) {
				colonyName = quarter.getColonyName();
				vacancyList = SQVacancyMap.get(colonyName);
				colonyList = SQMap.get(colonyName);
				quarterMap = new HashMap<String, QuarterStats>();
				colonyList.add(quarterMap);
				// TODO -- ADD COLONY STATS TO SQ COLONY LIST
			}
			quarterMap.put(quarter.getQuarterNo(), quarter);
			List<String> quarterList = vacancyList.get(quarter
					.getQuarterVacancy());
			if (quarterList == null) {
				quarterList = new ArrayList<String>();
				vacancyList.put(quarter.getQuarterVacancy(), quarterList);
			}
			quarterList.add(quarter.getQuarterNo());
		}
		
	}

	@SuppressWarnings("unchecked")
	private void populateMapsWithPQandODStats() {

		List<ColonyStats> colonyStats = daoHandler.getPQAccomodationStats();

		HashMap<String, ColonyStats> PQMap = (HashMap<String, ColonyStats>) headcountMap
				.get(PRIVATE_QUARTERS);

		HashMap<String, ColonyStats> ODMap = (HashMap<String, ColonyStats>) headcountMap
				.get(OUTSIDE_DAYALBAGH);
		for (ColonyStats colonyStat : colonyStats) {
			String colonyName = colonyStat.getColonyName();
			if (colonyName.equals(OTHER) || colonyName.equals(ADAN_BAGH)
					|| colonyName.equals(RADHA_NAGAR)) {
				ODMap.put(colonyName, colonyStat);
			} else {
				PQMap.put(colonyName, colonyStat);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void populateMapsWithSadanStats() {
		List<SadanStats> sadanStats = daoHandler.getSadanAccomodationStats();

		HashMap<String, SadanStats> sadanMap = (HashMap<String, SadanStats>) headcountMap
				.get(DORMITORIES);

		for (SadanStats sadanStat : sadanStats) {
			if (sadanStat.getOccupancy() == null) {
				sadanStat.setChildCount(0);
				sadanStat.setFemaleCount(0);
				sadanStat.setMaleCount(0);
				sadanStat.setOccupancy(0);
				sadanStat.setVacancy(sadanStat.getCapacity());
			}
			String sadanName = sadanStat.getSadanName();
			sadanMap.put(sadanName, sadanStat);
		}
	}
}
