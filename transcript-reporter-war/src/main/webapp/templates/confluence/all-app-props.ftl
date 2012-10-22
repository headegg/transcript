<#escape x as x?html>
<div class="properties-table-wrap table-wrap">
    <table class="properties-table confluenceTable">
        <thead>
        <tr>
            <th class="application-name confluenceTh">${application}</th>
            <#list environments as environment>
                <th class="environment-name confluenceTh">${environment}</th>
            </#list>
        </tr>
        </thead>
        <tbody>
            <#list keys as key>
            <tr>
                <th class="property-key confluenceTh">${key.id}</th>
                <#list key.values as value>
                    <td class="property-value <#if ! value??>un</#if>defined-property-value confluenceTd">${value!""}</td>
                </#list>
            </tr>
            </#list>
        </tbody>
    </table>
</div>
</#escape>
