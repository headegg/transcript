<#macro property class environment application key default="">
    <#compress>
        <#attempt>
            <#assign value = getProperties(environment, application, parameters.includeVersionControlProperties)[key]>
            <#if value?length != 0>
            <div class="defined-${class}">${value}</div>
            <#else>
            <div class="blank-${class}">${default}</div>
            </#if>
            <#recover>
            <div class="undefined-${class}">${default}</div>
        </#attempt>
    </#compress>
</#macro>

<#macro taggedProperty class environment application key default="">
    <#compress>
        <#attempt>
            <#assign value = getProperties(environment, application, false)[key]>
            <#if value?length != 0>
            <div class="${value}-${class}">${value}</div>
            <#else>
            <div class="blank-${class}">${default}</div>
            </#if>
            <#recover>
            <div class="undefined-${class}">${default}</div>
        </#attempt>
    </#compress>
</#macro>

<#macro nameAndEmail class type environment application key default="">
    <#compress>
        <#attempt>
            <#assign id = getProperties(environment, application, false)[key]>
            <#attempt>
                <#list dictionary[type][id].people as id>
                <div class="defined-${class}"><a href="mailto:${id.email}">${id.name}</a></div>
                </#list>
                <#recover>
                <div class="partly-defined-${class}">${id}</div>
            </#attempt>
            <#recover>
            <div class="undefined-${class}">${default}</div>
        </#attempt>
    </#compress>
</#macro>
