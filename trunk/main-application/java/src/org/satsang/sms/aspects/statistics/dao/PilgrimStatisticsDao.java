/**
 * 
 */
package org.satsang.sms.aspects.statistics.dao;

import java.util.List;

import org.satsang.sms.aspects.statistics.ColonyStats;
import org.satsang.sms.aspects.statistics.QuarterStats;
import org.satsang.sms.aspects.statistics.SadanStats;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

/**
 * @author Default
 * 
 */
public class PilgrimStatisticsDao implements IPilgrimStatisticsDao {

	private SqlMapClientTemplate sqlMapClientTemplate;

	@Override
	public void setSqlMapClientTemplate(
			SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<QuarterStats> getSQAccomodationStats() {
		List<QuarterStats> colonyStats = this.sqlMapClientTemplate.queryForList("PilgrimStatistics.SQAccomodationStats");
		return colonyStats;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ColonyStats> getPQAccomodationStats() {
		List<ColonyStats> colonyStats = this.sqlMapClientTemplate.queryForList("PilgrimStatistics.PQAccomodationStats");
		return colonyStats;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SadanStats> getSadanAccomodationStats() {
		List<SadanStats> sadanStats = this.sqlMapClientTemplate.queryForList("PilgrimStatistics.SadanAccomodationStats");
		return sadanStats;
	}

}
