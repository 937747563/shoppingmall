package com.zh.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zh.common.utils.PageUtils;
import com.zh.member.entity.MemberEntity;

import java.util.Map;

/**
 * ??Ա
 *
 * @author ZH
 * @email 937747563@qq.com
 * @date 2020-10-24 19:49:38
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

