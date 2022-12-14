package th.co.readypaper.billary.repo.utils;

import com.nimbusds.jose.shaded.json.JSONArray;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class CompanyCode {

    public static String get() {
        String companyCode = "";

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        JSONArray groups = jwt.getClaim("groups");

        if (groups != null && groups.size() > 0) {
            companyCode = (String) groups.get(0);
        }

        return companyCode.substring(1);
    }

}
