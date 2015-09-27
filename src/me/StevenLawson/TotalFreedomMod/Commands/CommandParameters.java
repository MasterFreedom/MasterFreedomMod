package me.StevenLawson.TotalFreedomMod.Commands;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandParameters
{
  String description();
  
  String usage();
  
  String aliases() default "";
}


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\CommandParameters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */