if (typeof MyCo == "undefined" || !MyCo)
{
   var MyCo = {};
}

/**
 * RuleConfigActionCustom.
 *
 * @namespace MyCo
 * @class MyCo.RuleConfigActionCustom
 */
(function()
{

   /**
    * YUI Library aliases
    */
   var Dom = YAHOO.util.Dom,
      Selector = YAHOO.util.Selector,
      Event = YAHOO.util.Event;

   /**
    * Alfresco Slingshot aliases
    */
    var $html = Alfresco.util.encodeHTML,
       $hasEventInterest = Alfresco.util.hasEventInterest;
   
   MyCo.RuleConfigActionCustom = function(htmlId)
   {
      MyCo.RuleConfigActionCustom.superclass.constructor.call(this, htmlId);

      // Re-register with our own name
      this.name = "MyCo.RuleConfigActionCustom";
      Alfresco.util.ComponentManager.reregister(this);

      // Instance variables
      this.customisations = YAHOO.lang.merge(this.customisations, MyCo.RuleConfigActionCustom.superclass.customisations);
      this.renderers = YAHOO.lang.merge(this.renderers, MyCo.RuleConfigActionCustom.superclass.renderers);
      
      return this;
   };

   YAHOO.extend(MyCo.RuleConfigActionCustom, Alfresco.RuleConfigAction,
   {

      /**
       * CUSTOMISATIONS
       */

      customisations:
      {         
         MoveReplaced:
         {
            text: function(configDef, ruleConfig, configEl)
            {
	             // Display as path
	             //this._getParamDef(configDef, "destination-folder")._type = "path";
	            // this._hideParameters(configDef.parameterDefinitions);
	             return configDef;
            },
            edit: function(configDef, ruleConfig, configEl)
            {
                // Hide all parameters since we are using a cusotm ui but set default values
                //this._hideParameters(configDef.parameterDefinitions);

               //  Make parameter renderer create a "Destination" button that displays an destination folder browser
               // configDef.parameterDefinitions.push({
               //    type: "arca:destination-dialog-button",
               //    displayLabel: this.msg("label.to"),
               //    _buttonLabel: this.msg("button.select-folder"),
               //    _destinationParam: "destination-folder"
               // });
                return configDef;
            }
         },
      },

   });

})();
