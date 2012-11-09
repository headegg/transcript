<#include "macros.ftl">
<#escape x as x?html>
<div <#if reportId??>id="${reportId}" </#if>class="properties-table table-wrap">
    <table class="confluenceTable">
        <thead>
        <tr>
            <th class="application-name confluenceTh"><@label type="applications" id=parameters.application /></th>
            <#list reportableEnvironments as environment>
                <th class="environment-name confluenceTh"><@label type="environments" id=environment /></th>
            </#list>
        </tr>
        </thead>
        <tbody>
            <#list reportableKeys as key>
            <tr>
                <th class="property-key confluenceTh">${key}</th>
                <#list reportableEnvironments as environment>
                    <td class="property-value confluenceTd"><@property class="property-value" environment=environment application=parameters.application key=key /></td>
                </#list>
            </tr>
            </#list>
        </tbody>
    </table>
</div>
</#escape>
