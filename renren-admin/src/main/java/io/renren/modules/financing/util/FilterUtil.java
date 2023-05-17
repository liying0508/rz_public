//package io.renren.modules.financing.util;
//
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//
//public class FilterUtil {
//
//    private static final Long SUPER_ADMIN_ACCOUNT = 1067246875800000001L;
//
//    public static void filterData(){
//        ServletRequestAttributes requestAttributes
//                = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = requestAttributes.getRequest();
//        Long userId = shiroService.getByToken(request.getHeader("token")).getUserId();
//
//        if (userId != 1067246875800000001L){
//            List<Long> roleIdList = sysRoleUserService.getRoleIdList(userId);
//            for (Long aLong : roleIdList) {
//                List<Long> deptIdList = sysRoleDataScopeService.getDeptIdList(aLong);
//                list.removeIf(next -> !deptIdList.contains(next.getDeptId()));
//            }
//        }
//    }
//}
