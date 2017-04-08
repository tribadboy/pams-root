package com.nju.pams.biz.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nju.pams.biz.service.PamsAccountService;
import com.nju.pams.mapper.dao.PamsAccountDAO;
import com.nju.pams.mapper.dao.PamsAccountMonthDAO;
import com.nju.pams.model.consumption.AccountOfMonth;
import com.nju.pams.model.consumption.ConsumptionAccount;
import com.nju.pams.model.consumption.ConsumptionCondition;
import com.nju.pams.util.DateUtil;

@Service
@Transactional(propagation=Propagation.REQUIRED)
public class PamsAccountServiceImpl implements PamsAccountService {
	
	private static final Logger logger = Logger.getLogger(PamsAccountServiceImpl.class);
	
	@Autowired
	PamsAccountDAO pamsAccountDAO;
	
	@Autowired
	PamsAccountMonthDAO pamsAccountMonthDAO;

	@Override
	public ConsumptionAccount getConsumptionAccountByAccountId(Integer accountId) {
		return pamsAccountDAO.getConsumptionAccountByAccountId(accountId);
	}

	@Override
	public List<ConsumptionAccount> getConsumptionAccountListByUserId(Integer userId) {
		return pamsAccountDAO.getConsumptionAccountListByUserId(userId);
	}

	@Override
	public List<ConsumptionAccount> selectAccountByCondition(ConsumptionCondition consumptionCondition) {
		return pamsAccountDAO.selectAccountByCondition(consumptionCondition);
	}
	
	/**
	 * 插入某一条用户账目，同时更新用户月份总和账目
	 */
	@Override
	public void insertConsumptionAccount(ConsumptionAccount consumptionAccount) {
		BigDecimal cost = consumptionAccount.getCost();
		//插入当前单笔账目
		pamsAccountDAO.insertConsumptionAccount(consumptionAccount);
		//根据单笔账目查询当月总和账目
		AccountOfMonth accountOfMonth = getMonthAccountBySingleAccount(consumptionAccount);
		
		if(null == accountOfMonth) {
			//没有当月记录，创建一条新的记录插入
			int userId = consumptionAccount.getUserId();
			int consumptionId = consumptionAccount.getConsumptionId();		
			String spendMonth = consumptionAccount.getSpendTime().substring(0, 7);
			int daysOfMonth = DateUtil.getDaysInMonth(spendMonth);
			pamsAccountMonthDAO.insertAccountOfMonth(new AccountOfMonth(userId, consumptionId, 
					cost, spendMonth, 1, daysOfMonth));
			
		} else {
			//更新当月总和账目的cost和numberOfAccount
			accountOfMonth.setCost(accountOfMonth.getCost().add(cost));
			accountOfMonth.setNumberOfAccount(accountOfMonth.getNumberOfAccount() + 1);
			pamsAccountMonthDAO.updateAccountOfMonth(accountOfMonth);
		}
	}
	
	/**
	 * 修改单笔账目，同时修改月份账目总和
	 * @param consumptionAccount
	 */
	@Override
	public void updateConsumptionAccount(ConsumptionAccount consumptionAccount) {	
		//删除旧的单笔账目
		deleteConsumptionAccountByAccountId(consumptionAccount.getAccountId());
		//添加新的单笔账目
		insertConsumptionAccount(consumptionAccount);
	}
	
	/**
	 * 删除单笔账目，同时修改月份账目总和
	 * @param accountId
	 */
	@Override
	public void deleteConsumptionAccountByAccountId(Integer accountId) {
		//删除前先获取单笔账目的数据
		ConsumptionAccount account = pamsAccountDAO.getConsumptionAccountByAccountId(accountId);
		//删除数据库中的单笔账目
		pamsAccountDAO.deleteConsumptionAccountByAccountId(accountId);
		//根据单笔账目获取当月总和账目
		AccountOfMonth accountOfMonth = getMonthAccountBySingleAccount(account);
		if(null == accountOfMonth) {
			logger.error("删除单笔账目时没有找到对应的月份总和账目");
		} else {
			int num = accountOfMonth.getNumberOfAccount();
			BigDecimal singleCost = account.getCost();
			if(num == 1) {
				//月份总和账目由一笔账目组成，则删除记录
				pamsAccountMonthDAO.deleteAccountOfMonthById(accountOfMonth.getId());			
			} else if (num > 1) {
				//月份总和账目由多笔账目组成，更新记录
				accountOfMonth.setNumberOfAccount(num - 1);
				accountOfMonth.setCost(accountOfMonth.getCost().subtract(singleCost));
				pamsAccountMonthDAO.updateAccountOfMonth(accountOfMonth);
			} else {
				logger.error("删除单笔账目时，月份总额账目不为空，但账目计数小于1");
			}
		}	
	}
	
	/**
	 * 根据单笔账目查询对应的月份总和账目
	 * @param account
	 * @return
	 */
	public AccountOfMonth getMonthAccountBySingleAccount(ConsumptionAccount consumptionAccount) {
		if(null == consumptionAccount) {
			return null;
		} else {
			int userId = consumptionAccount.getUserId();
			int consumptionId = consumptionAccount.getConsumptionId();
			String spendMonth = consumptionAccount.getSpendTime().substring(0, 7);
			return pamsAccountMonthDAO.getAccountOfMonthByMonth(userId, consumptionId, spendMonth);
		}
	}
	
}
