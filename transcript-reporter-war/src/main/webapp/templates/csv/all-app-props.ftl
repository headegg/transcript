${application}<#list environments as environment>,${environment}</#list>
<#list keys as key>${key.id}<#list key.values as value>,${value!"[undefined]"}</#list>
</#list>
