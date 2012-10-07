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
            <th class="confluenceTh"><code>${key.id}</code></th>
            <#list key.values as value>
                <td class="confluenceTd"><code>${value!"[undefined]"}</code></td>
            </#list>
        </tr>
        </#list>
        </tbody>
    </table>
</div>
