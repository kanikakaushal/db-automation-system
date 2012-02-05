/**
 * 
 */
package org.satsang.sms.core.util.print;

import java.util.List;

import org.satsang.sms.core.activities.tvc.TvcAssociateMember;
import org.satsang.sms.core.activities.tvc.TvcMember;
import org.satsang.sms.core.event.interfaces.IEvent;

/**
 * @author Default
 *
 */
public interface IPrinter {

	void print(IEvent event, TvcMember tvc, List<TvcAssociateMember> tvcAssociations) throws Exception;
	
	String getPrinterName();
	
	String getPrintDirectory();	
	
}
