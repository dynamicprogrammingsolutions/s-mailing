
var moduleBase = "./scripts";
var loadedModules = {};
var moduleCallbacks = {};

function loadModule(module,cbModuleLoaded,cbModuleFail)
{
    if (moduleCallbacks[module] === undefined) moduleCallbacks[module] = [];
    moduleCallbacks[module].push(cbModuleLoaded);
    console.log("starting to load module: "+module);
    $.ajax({
        url: moduleBase+"/"+module+".js",
        cache: true,
        success: function () {
            //cbModuleLoaded(module)
            console.log("module file loaded: ",module);
        },
        error: function() {
            cbModuleFail(module);
        }
    })
}

function module(name,requires,module)
{

    var loaded = false;
    var dependencies = {};

    function localRequire(moduleName) {
        return dependencies[moduleName];
    }

    function checkReady() {
        console.log("checking dependencies for: ",name);
        for (r in requires) {
            if (loadedModules[requires[r]] == undefined) return false;
            else {
                console.log("module loaded: ",requires[r],loadedModules[requires[r]])
            }
        }
        console.log("dependencies ok for: ",name);
        return true;
    }

    function load() {
        if (!loaded) {
            loaded = true;
            console.log("starting module "+name);
            loadedModules[name] = module(localRequire);
            console.log("module loaded: "+name);
            if (moduleCallbacks[name] !== undefined) {
                for (var c in moduleCallbacks[name]) {
                    moduleCallbacks[name][c](name,loadedModules[name]);
                }
            }
        }
    }

    if (requires.length == 0) {
        load();
    } else {
        var missing = false;
        for (r in requires) {
            if (loadedModules[requires[r]] === undefined) {
                missing = true;
                loadModule(requires[r], function (loadedModuleName, loadedModule) {
                    if (loadedModule != undefined) {
                        dependencies[loadedModuleName] = loadedModule;
                    }
                    if (checkReady()) {
                        load();
                    }
                }, function (failedModuleName) {
                    console.log("failed to load: " + failedModuleName);
                });
            } else {
                dependencies[requires[r]] = loadedModules[requires[r]];
            }
        }
        if (!missing) {
            load(localRequire);
        }
    }

}