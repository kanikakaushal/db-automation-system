/**
 * 
 */
package org.satsang.sms.aspects.statistics.interfaces;

import org.satsang.sms.aspects.statistics.dao.IPilgrimStatisticsDao;

/**
 * @author Default
 *
 */
public interface IPilgrimStatistics extends IRealTimeStatistics {

	public void setDaoHandler(IPilgrimStatisticsDao daoHandler);
}
