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
public interface IPilgrimStatisticsDao {

	public void setSqlMapClientTemplate(
			SqlMapClientTemplate sqlMapClientTemplate);

	public List<QuarterStats> getSQAccomodationStats();
	
	public List<ColonyStats> getPQAccomodationStats();
	
	public List<SadanStats> getSadanAccomodationStats();
}
