<#escape x as x?html>
<div class="table-wrap">
    <table class="confluenceTable">
        <thead>
        <tr>
            <th class="confluenceTh"/>
            <#list environments as environment>
                <th class="confluenceTh">${environment}</th>
            </#list>
        </tr>
        </thead>
        <tbody>
            <#list keys as key>
            <tr>
                <th class="confluenceTh">${key.id}</th>
                <#list key.values as value>
                    <td class="confluenceTd">${value!"[undefined]"}</td>
                </#list>
            </tr>
            </#list>
        </tbody>
    </table>
</div>
</#escape>
