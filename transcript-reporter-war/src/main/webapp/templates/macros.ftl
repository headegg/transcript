<#macro label type id>
    <#compress>
        <#attempt>${dictionary[type][id].name}<#recover>${id}</#attempt>
    </#compress>
</#macro>

<#macro property class environment application key>
    <#compress>
        <#attempt>
            <#assign value = getProperties(environment, application)[key]>
            <#if value?length != 0>
            <span class="defined-${class}">${value}</span>
            <#else>
            <span class="blank-${class}"></span>
            </#if>
            <#recover>
            <span class="undefined-${class}"></span>
        </#attempt>
    </#compress>
</#macro>

<#macro nameAndEmail class type environment application key>
    <#compress>
        <#attempt>
            <#assign id = getProperties(environment, application)[key]>
            <#attempt>
                <#list dictionary[type][id].people as id>
                <div class="defined-${class}"><a href="mailto:${id.email}">${id.name}</a></div>
                </#list>
                <#recover>
                <span class="partly-defined-${class}">${id}</span>
            </#attempt>
            <#recover>
            <span class="undefined-${class}"></span>
        </#attempt>
    </#compress>
</#macro>