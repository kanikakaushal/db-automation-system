package org.satsang.sms.core.member.dao;

import java.util.HashMap;
import java.util.Map;

import org.satsang.sms.core.member.interfaces.IMember;
import org.satsang.sms.core.member.interfaces.IMemberDao;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

public class MemberIbatisDao implements IMemberDao {

	private SqlMapClientTemplate sqlMapClientTemplate;

	public void setSqlMapClientTemplate(
			SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	@Override
	public IMember getMember(String memberId) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("memberId", memberId);

		return (IMember) sqlMapClientTemplate.queryForObject(
				"Member.getMemberByMemberId", parameters);
	}

}
