/**
 * 
 */
package org.satsang.sms.core.member;

import org.satsang.sms.core.member.interfaces.IMember;
import org.satsang.sms.core.member.interfaces.IMemberDao;
import org.satsang.sms.core.member.interfaces.IMemberHandler;

/**
 * @author Default
 *
 */
public class MemberHandler implements IMemberHandler{

	private IMemberDao memberDao;

	public void setMemberDao(IMemberDao memberDao) {
		this.memberDao = memberDao;
	}
	
	public IMember memberDetails(String memberId){
		return memberDao.getMember(memberId);
	}
}
