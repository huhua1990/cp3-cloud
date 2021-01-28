package com.cp3.cloud.flowable.controller.rest;

import org.flowable.engine.ManagementService;
import org.flowable.idm.api.IdmIdentityService;
import org.flowable.idm.api.User;
import org.flowable.ui.common.model.ResultListDataRepresentation;
import org.flowable.ui.common.model.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/app")
public class EditorUsersResource {

    @Autowired
    protected IdmIdentityService idmIdentityService;
    @Autowired
    protected ManagementService managementService;

    @RequestMapping(value = "/rest/editor-users", method = RequestMethod.GET)
    public ResultListDataRepresentation getUsers(@RequestParam(value = "filter", required = false) String filter) {
        if (!StringUtils.isEmpty(filter)) {
            filter = filter.trim();
            String sql = "select * from act_id_user where ID_ like #{id} or LAST_ like #{name} limit 10";
            filter = "%"+filter+"%";
            List<User> matchingUsers = idmIdentityService.createNativeUserQuery().sql(sql).parameter("id",filter).parameter("name",filter).list();
            List<UserRepresentation> userRepresentations = new ArrayList<>(matchingUsers.size());
            for (User user : matchingUsers) {
                userRepresentations.add(new UserRepresentation(user));
            }
            return new ResultListDataRepresentation(userRepresentations);
        }
       return null;
    }

}
