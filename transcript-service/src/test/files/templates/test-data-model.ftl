<#assign environments = getEnvironments("")>
{ all environments
<#list environments as environment>
<${environment}>
</#list>
}
{ environments starting with D
<#list getEnvironments("^d.*") as environment>
<${environment}>
</#list>
}
{ really all environments
<#list getEnvironments(".*") as environment>
<${environment}>
</#list>
}
{ all applications in all environments
<#list getApplications(environments, "") as application>
<${application}>
</#list>
}
{ all applications in all environments that start with acme-s
<#list getApplications(environments, "^acme-s.*") as application>
<${application}>
</#list>
}
{ really all applications in all environments
<#list getApplications(environments, ".*") as application>
<${application}>
</#list>
}
{ all environments with acme-service defined
<#assign environments = getEnvironmentsWithApplications(environments, ["acme-service"], ".*", false)>
<#list environments as environment>
<${environment}>
</#list>
}
<#assign applications = getApplicationsWithEnvironments(["dev"], ["acme-service", "undefined-application"], ".*", false)>
{ Applications that are defined in dev
<#list applications as application>
<${application}>
</#list>
}
<#assign keys = getKeys(environments, getApplications(environments, ""), ".*", false)>
{ all defined keys
<#list keys as key>
<${key}>
</#list>
}
