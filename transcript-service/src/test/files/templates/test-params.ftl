<${parameters.workingCopyPath}>
<${parameters.workingCopyId}>
<${parameters.readOnly?string}>
<${parameters.useCache?string}>
<${parameters.includeVersionControlProperties?string}>
<${parameters.defaultValue}>
<${parameters.merge?string}>
<${parameters.failOnValueChange?string}>
<${parameters.notDefined}>
<${parameters.get("notDefined", "other-def")}>
<${parameters.extraParam}>
<${parameters["extra-param"]}>
<${parameters.get("true-param", true)?string}>
<${parameters.get("true-param", false)?string}>
<${parameters.get("false-param", true)?string}>
<${parameters.get("false-param", false)?string}>
<${parameters.get("bad-boolean-param", true)?string}>
<${parameters.get("bad-boolean-param", false)?string}>
<${parameters.get("not-defined", true)?string}>
<${parameters.get("not-defined", false)?string}>
<${parameters.setWorkingCopyId("test-working-copy")}>
<${parameters.workingCopyId}>
