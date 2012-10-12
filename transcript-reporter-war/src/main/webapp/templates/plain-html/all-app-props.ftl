<#escape x as x?html>
<div>
    <table>
        <thead>
        <tr>
            <th>${application}</th>
            <#list environments as environment>
                <th>${environment}</th>
            </#list>
        </tr>
        </thead>
        <tbody>
            <#list keys as key>
            <tr>
                <th
                ">${key.id}</th>
                <#list key.values as value>
                    <td>${value!"[undefined]"}</td>
                </#list>
            </tr>
            </#list>
        </tbody>
    </table>
</div>
</#escape>
